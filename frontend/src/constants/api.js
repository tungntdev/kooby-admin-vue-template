const API = {
    AUTH: {
        GENERATE_TOKEN: '/auth/generate-token'
    },

    PATIENT_REQUEST: {
        SEARCH: '/secure/patient-request/search',
        CREATE: '/secure/patient-request/create',
        UPDATE: '/secure/patient-request/update',
        DELETE: '/secure/patient-request/delete/',
        NEXT_PATIENT_ORDER: '/secure/patient-request/next-order',
        NEXT_DELIVERY_ORDER: '/secure/patient-request/next-delivery-order',
        SET_SIGNED: '/secure/patient-request/signed/',
        SET_DELIVERED: '',
        SET_RECEIVED: '/secure/patient-request/received/'
    },

    DEATH_RECORD: {
        SEARCH: '/secure/death-records/search-record',
        CREATE: '/secure/death-records/create',
        UPDATE: '/secure/death-records/update',
        DELETE: '/secure/death-records/delete/',
        GET_NUMBER: '/secure/death-records/number'
    },

    STORAGE: {
        SEARCH: '/secure/storage/search',
        CREATE: '/secure/storage/create',
        UPDATE: '/secure/storage/update/',
        DELETE: '/secure/storage/delete/',
        GET_NUMBER: '/secure/storage/find-next-save-number'
    },

    COMMON: {
        LOAD_DEPARTMENTS: '/secure/departments',
        LOAD_PROVINCES: '/secure/address/province',
        LOAD_DISTRICTS_BY_PROVINCE: '/secure/address/district/province/',
        LOAD_DISTRICT_BY_DISTRICT: '/secure/address/district/'
    }
};

export default API;
