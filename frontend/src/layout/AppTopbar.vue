<script setup>
import { useLayout } from '@/layout/composables/layout';
import AppConfigurator from './AppConfigurator.vue';
import LangPalette from '@/components/LangPalette.vue';
import { useConfirm } from 'primevue/useconfirm';
import { useRouter } from 'vue-router';
import App from '@/constants/app';

const { onMenuToggle, toggleDarkMode, isDarkTheme } = useLayout();
const confirm = useConfirm();

const router = useRouter();
const confirmationLogout = async () => {
    confirm.require({
        group: 'headless',
        message: 'Are you want to logout?',
        accept: async () => {
            localStorage.removeItem('AppAccessToken');
            localStorage.removeItem('AppAccessExpiration');
            window.location.reload();
        },
        reject: () => {
            console.debug('Cancel');
        }
    });
};
const avatarUrl = `avatars/${localStorage.getItem(App.PROFILE.PROFILE_URL)}-min.jpg`;
</script>

<template>
    <div class="layout-topbar">
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" @click="onMenuToggle">
                <i class="pi pi-bars"></i>
            </button>
            <router-link class="layout-topbar-logo" to="/">
                <img src="/demo/images/logo-white.png" style="width: auto; height: 40px" />
            </router-link>
        </div>

        <div class="layout-topbar-actions">
            <div class="layout-config-menu">
                <button class="layout-topbar-action" type="button" @click="toggleDarkMode">
                    <i :class="['pi', { 'pi-moon': isDarkTheme, 'pi-sun': !isDarkTheme }]"></i>
                </button>

                <div class="relative">
                    <button
                        v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
                        class="layout-topbar-action layout-topbar-action-highlight"
                        type="button"
                    >
                        <i class="pi pi-palette"></i>
                    </button>
                    <AppConfigurator :show-menu-mode="true" />
                </div>
            </div>

            <lang-palette />

            <button
                v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
                class="layout-topbar-menu-button layout-topbar-action"
            >
                <i class="pi pi-ellipsis-v"></i>
            </button>

            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">
                    <button class="layout-topbar-action" type="button">
                        <i class="pi pi-calendar"></i>
                        <span>Calendar</span>
                    </button>
                    <button class="layout-topbar-action" type="button">
                        <i class="pi pi-inbox"></i>
                        <span>Messages</span>
                    </button>
                    <button class="layout-topbar-action" type="button" @click="confirmationLogout($event)">
                        <Avatar :image="avatarUrl" shape="circle" />
                        <span>Profile</span>
                    </button>
                    <ConfirmPopup group="headless">
                        <template #container="{ message, acceptCallback, rejectCallback }">
                            <div class="rounded p-4">
                                <span>{{ message.message }}</span>
                                <div class="flex items-center gap-2 mt-4">
                                    <Button label="Logout" @click="acceptCallback" size="small"></Button>
                                    <Button label="Cancel" outlined @click="rejectCallback" severity="secondary" size="small" text></Button>
                                </div>
                            </div>
                        </template>
                    </ConfirmPopup>
                </div>
            </div>
        </div>
    </div>
</template>
