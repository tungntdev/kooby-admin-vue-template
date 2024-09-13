const PageSpec = {
    AUTH: {
        LOGIN: {
            path: '/auth/login',
            name: 'login',
            meta: {
                title: 'page.menu-title.auth.login',
                permissions: []
            },
            component: () => import('@/views/pages/auth/Login.vue')
        }
    },
    ACCESS: {
        NOT_FOUND: {
            path: '/pages/notfound',
            name: 'notfound',
            meta: {
                title: 'page.menu-title.page.notfound',
                permissions: []
            },
            component: () => import('@/views/pages/NotFound.vue')
        },
        DENIED: {
            path: '/auth/access',
            name: 'accessDenied',
            meta: {
                title: 'page.menu-title.page.access-denied',
                permissions: []
            },
            component: () => import('@/views/pages/auth/Access.vue')
        },
        ERROR: {
            path: '/auth/error',
            name: 'accessError',
            meta: {
                title: 'page.menu-title.page.access-error',
                permissions: []
            },
            component: () => import('@/views/pages/auth/Error.vue')
        }
    },
    APP: {
        DASHBOARD: {
            path: '/',
            name: 'dashboard',
            meta: {
                title: 'page.menu-title.page.dashboard',
                permissions: []
            },
            component: () => import('@/views/Dashboard.vue')
        },
        PATIENT_RECEPTION: {
            RECEPTION: {
                path: '/patient/reception',
                name: 'reception',
                meta: {
                    title: 'page.menu-title.page.patient-reception',
                    permissions: []
                },
                component: () => import('@/views/reception/PatientReception.vue')
            }
        },
        DEATH_NUMBER: {
            path: '/death/manager',
            name: 'death',
            meta: {
                title: 'page.menu-title.page.number-allocation',
                permissions: []
            },
            component: () => import('@/views/deathnumber/Manager.vue')
        },
        STORAGE: {
            path: '/storage/reception',
            name: 'storage',
            meta: {
                title: 'page.menu-title.page.medical-record-storage',
                permissions: []
            },
            component: () => import('@/views/storage/Manager.vue')
        },
        PAGE: {
            FORM_LAYOUT: {
                path: '/uikit/formlayout',
                name: 'formlayout',
                meta: {
                    title: 'page.menu-title.page.number-allocation',
                    permissions: []
                },
                component: () => import('@/views/uikit/FormLayout.vue')
            }
        }
    }
};

export default PageSpec;
