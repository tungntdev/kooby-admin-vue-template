const API = {
    AUTH: {
        GENERATE_TOKEN: '/auth/generate-token'
    },

    PATIENT_REQUEST: {
        SEARCH: '/secure/patientRequest/search'
    },

    DEATH_RECORD: {
        SEARCH: '/secure/death-records/searchRecord',
        CREATE: '/secure/death-records/create',
        UPDATE: '/secure/death-records/update',
        DELETE: '/secure/death-records/delete/',
        GET_NUMBER: '/secure/death-records/number'
    }
};

export default API;
