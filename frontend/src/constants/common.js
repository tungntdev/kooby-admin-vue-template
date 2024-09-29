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

    CONFIRM_TYPE: {
        DELETE: 'delete',
        SIGNED: 'signed',
        RECEIVED: 'received',
        DELIVERED: 'delivered',
        COPIED: 'copied'
    },

    ROLES: {
        USER: 'USER',
        ADMIN: 'ADMIN',
        SECRETARY: 'SECRETARY',
        COPY_MAN: 'COPY_MAN',
        VALIDATOR: 'VALIDATOR',
        LEADER: 'LEADER',
        DATA_ENTRY_PERSON: 'DATA_ENTRY_PERSON',
        MANAGER: 'MANAGER',
        DOCUMENT_ISSUER: 'DOCUMENT_ISSUER'
    },

    DEFAULT_ROW_PAGE: 10
};

export default COMMON;
