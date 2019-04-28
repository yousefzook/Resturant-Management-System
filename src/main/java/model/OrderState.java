package model;

public enum OrderState {
    inQueue {
        @Override
        public String toString() {
            return "IN_QUEUE";
        }
    }, Assigned {
        @Override
        public String toString() {
            return "ASSIGNED";
        }
    }, Done {
        @Override
        public String toString() {
            return "DONE";
        }
    }
}
