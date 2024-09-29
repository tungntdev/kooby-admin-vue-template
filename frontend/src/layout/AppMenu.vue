<script setup>
import { computed, ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';
import App from '@/constants/app';
import COMMON from '@/constants/common';

const model = ref([
    {
        label: 'page.menu-bar.home',
        items: [
            {
                label: 'page.menu-bar.home.dashboard',
                icon: 'pi pi-fw pi-chart-scatter',
                to: '/',
                requiredRole: COMMON.ROLES.USER
            }
        ]
    },
    {
        label: 'page.menu-bar.patient-reception',
        items: [
            {
                label: 'page.menu-bar.patient-reception.reception',
                icon: 'pi pi-fw pi-pen-to-square',
                to: '/patient/reception',
                requiredRole: COMMON.ROLES.DATA_ENTRY_PERSON
            },
            {
                label: 'page.menu-bar.patient-reception.list-report',
                icon: 'pi pi-fw pi-clipboard',
                to: '/patient/report-request',
                requiredRole: COMMON.ROLES.DATA_ENTRY_PERSON
            },
            {
                label: 'page.menu-bar.patient-reception.delivery-report',
                icon: 'pi pi-fw pi-list',
                to: '/patient/report-delivery',
                requiredRole: COMMON.ROLES.DATA_ENTRY_PERSON
            },
            {
                label: 'page.menu-bar.patient-reception.mailing-report',
                icon: 'pi pi-fw pi-list-check',
                to: '/patient/report-delivered',
                class: 'rotated-icon',
                requiredRole: COMMON.ROLES.DATA_ENTRY_PERSON
            }
        ],
        requiredRole: COMMON.ROLES.DATA_ENTRY_PERSON
    },
    {
        label: 'page.menu-bar.medical-record',
        icon: 'pi pi-fw pi-briefcase',
        items: [
            {
                label: 'page.menu-bar.medical-record.copy-list',
                icon: 'pi pi-fw pi-receipt',
                to: '/record/list',
                requiredRole: COMMON.ROLES.COPY_MAN
            },
            {
                label: 'page.menu-bar.medical-record.copy-medical-record',
                icon: 'pi pi-fw pi-print',
                to: '/record/copy'
            },
            {
                label: 'page.menu-bar.medical-record.borrow-medical-record',
                icon: 'pi pi-fw pi-bookmark',
                to: '/record/borrow'
            },
            {
                label: 'page.menu-bar.medical-record.refund-medical-record',
                icon: 'pi pi-fw pi-bookmark-fill',
                to: '/record/refund'
            }
        ],
        requiredRole: COMMON.ROLES.COPY_MAN
    },
    {
        label: 'page.menu-bar.number-allocation',
        items: [
            { label: 'page.menu-bar.number-allocation.transfer', icon: 'pi pi-fw pi-id-card', to: '/uikit/formlayout' },
            {
                label: 'page.menu-bar.number-allocation.death-certificate',
                icon: 'pi pi-fw pi-id-card',
                to: '/death/manager',
                requiredRole: COMMON.ROLES.DOCUMENT_ISSUER
            }
        ],
        requiredRole: COMMON.ROLES.DOCUMENT_ISSUER
    },
    {
        label: 'page.menu-bar.medical-record-storage',
        items: [
            {
                label: 'page.menu-bar.medical-record-storage.storage-number',
                icon: 'pi pi-fw pi-server',
                to: '/storage/reception',
                requiredRole: COMMON.ROLES.MANAGER
            }
        ],
        requiredRole: COMMON.ROLES.MANAGER
    }
]);
const userRoles = localStorage.getItem(App.ACCESS.PERMISSIONS);
const filteredMenu = computed(() => {
    return model.value
        .map((menuItem) => {
            const filteredItems = menuItem.items.filter((item) => userRoles.includes(item.requiredRole));
            if (filteredItems.length > 0) {
                return { ...menuItem, items: filteredItems };
            }
            return null;
        })
        .filter((menuItem) => menuItem !== null);
});
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in filteredMenu" :key="item">
            <app-menu-item v-if="!item.separator" :index="i" :item="item"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
