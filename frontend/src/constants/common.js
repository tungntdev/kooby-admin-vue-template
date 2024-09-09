const COMMON = {
    STATES: {
        PENDING: 0,
        ASSIGNED: 1,
        IN_PROGRESS: 2,
        MANAGER_APPROVED: 3,
        DIRECTOR_APPROVED: 4,
        REJECTED: 5,
        COMPLETE: 6
    },

    STATUS: {
        ACTIVATED: 1,
        INACTIVATED: 0,
        LOCK: 3,
        DELETED: 9
    },

    DEFAULT_ROW_PAGE: 10
};

export default COMMON;
