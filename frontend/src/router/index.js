import AppLayout from '@/layout/AppLayout.vue';
import { createRouter, createWebHistory } from 'vue-router';
import page from '@/router/page';
import PageSpec from '@/router/page';
import { nextTick } from 'vue';
import { translate } from '@/locales';
import useAuthStore from '@/store/authStore';
import logger from '@/common/logger';
import App from '@/constants/app';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: AppLayout,
            children: [
                page.APP.DASHBOARD,

                page.APP.PATIENT_RECEPTION.RECEPTION,
                page.APP.PATIENT_RECEPTION.REPORT_REQUEST,
                page.APP.PATIENT_RECEPTION.REPORT_DELIVERY,
                page.APP.PATIENT_RECEPTION.REPORT_DELIVERED,

                page.APP.DEATH_NUMBER,

                page.APP.STORAGE,

                page.APP.RECORD.COPY_LIST,
                page.APP.RECORD.COPY_RECORD,
                page.APP.RECORD.BORROW,
                page.APP.RECORD.REFUND
            ]
        },
        page.AUTH.LOGIN,
        page.ACCESS.NOT_FOUND,
        page.ACCESS.ERROR,
        page.ACCESS.DENIED,
        page.APP.PAGE.FORM_LAYOUT
    ]
});

const whiteListUrl = [PageSpec.ACCESS.DENIED.name, PageSpec.ACCESS.NOT_FOUND.name, PageSpec.ACCESS.ERROR.name];

router.beforeEach(async (to, from, next) => {
    if (whiteListUrl.includes(to.name)) {
        next();
    } else if (isAuthenticated()) {
        await redirectIfValid(to, from, next);
    } else {
        await redirectIfInvalid(to, from, next);
    }
});

function isAuthenticated() {
    const authStore = useAuthStore();
    logger.debug(authStore.isAuthenticated);
    return authStore.isAuthenticated;
}

async function redirectIfInvalid(to, from, next) {
    if (to.path !== PageSpec.AUTH.LOGIN.path) {
        await next({
            name: PageSpec.AUTH.LOGIN.name
        });
    } else {
        next();
    }
}

async function redirectIfValid(to, from, next) {
    if (to.name === PageSpec.AUTH.LOGIN.name) {
        await next({ name: PageSpec.APP.DASHBOARD.name });
    } else if (hasPermission(to.meta.permissions ?? [])) {
        next();
    } else {
        await next({ name: PageSpec.ACCESS.DENIED.name });
    }
}

function hasPermission(permissions) {
    const authStore = useAuthStore();
    const userRoles = localStorage.getItem(App.ACCESS.PERMISSIONS);
    return userRoles.includes(permissions);
}

function containsAny(arr1, arr2) {
    return arr1.some((element) => {
        return arr2.indexOf(element) !== -1;
    });
}

router.afterEach(async (to) => {
    await nextTick(() => {
        const pageTitle = translate(to.meta.title ?? to.name.toUpperCase() + '');
        document.title = translate('page.menu-title.default') + ' - ' + pageTitle;
    });
});

export default router;
