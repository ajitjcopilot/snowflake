package com.ajit.snowflake.configs;

    /**
     * A Snowflake ID Generator for globally unique and time-ordered IDs.
     */
    public class SnowflakeIdGenerator {
        // Custom epoch (e.g., 2023-01-01T00:00:00Z)
        private final long epoch = 1672531200000L;

        // Bit allocations
        private final long nodeIdBits = 10L;        // 10 bits for Node ID
        private final long sequenceBits = 12L;     // 12 bits for Sequence Number

        // Max values for Node ID and Sequence
        private final long maxNodeId = ~(-1L << nodeIdBits);  // 2^10 - 1 = 1023
        private final long maxSequence = ~(-1L << sequenceBits);  // 2^12 - 1 = 4095

        // Bit shifts
        private final long nodeIdShift = sequenceBits;
        private final long timestampShift = sequenceBits + nodeIdBits;

        // Node-specific ID
        private final long nodeId;

        // State variables
        private long lastTimestamp = -1L; // Last timestamp used
        private long sequence = 0L;       // Sequence number within the same millisecond

        /**
         * Constructor for SnowflakeIdGenerator.
         *
         * @param nodeId Unique ID for the node/machine (0 to maxNodeId).
         */
        public SnowflakeIdGenerator(long nodeId) {
            if (nodeId < 0 || nodeId > maxNodeId) {
                throw new IllegalArgumentException("Node ID must be between 0 and " + maxNodeId);
            }
            this.nodeId = nodeId;
        }

        /**
         * Generates the next unique ID.
         *
         * @return A globally unique Snowflake ID.
         */
        public synchronized long nextId() {
            long currentTimestamp = getCurrentTimestamp();

            // Handle backward clock drift
            if (currentTimestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " +
                        (lastTimestamp - currentTimestamp) + " milliseconds");
            }

            if (currentTimestamp == lastTimestamp) {
                // Increment the sequence within the same millisecond
                sequence = (sequence + 1) & maxSequence;
                if (sequence == 0) {
                    // Sequence exhausted; wait for the next millisecond
                    currentTimestamp = waitForNextMillis(lastTimestamp);
                }
            } else {
                // Reset sequence for a new millisecond
                sequence = 0;
            }

            // Update the last timestamp
            lastTimestamp = currentTimestamp;

            // Generate the ID
            return ((currentTimestamp - epoch) << timestampShift) | (nodeId << nodeIdShift) | sequence;
        }

        /**
         * Gets the current system timestamp in milliseconds.
         *
         * @return Current timestamp in milliseconds.
         */
        private long getCurrentTimestamp() {
            return System.currentTimeMillis();
        }

        /**
         * Waits until the next millisecond when the clock moves forward.
         *
         * @param lastTimestamp The last recorded timestamp.
         * @return A valid timestamp greater than the last recorded timestamp.
         */
        private long waitForNextMillis(long lastTimestamp) {
            long timestamp = getCurrentTimestamp();
            while (timestamp <= lastTimestamp) {
                timestamp = getCurrentTimestamp();
            }
            return timestamp;
        }
    }
