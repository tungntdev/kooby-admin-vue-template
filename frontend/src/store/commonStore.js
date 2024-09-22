import { defineStore } from 'pinia';
import { ref } from 'vue';
import CommonService from '@/service/CommonService';

const commonService = CommonService.INSTANCE;
export const useCommonStore = defineStore('commonStore', () => {
    const departments = ref();
    const listProvinces = ref();
    const listCopyUsers = ref();

    async function loadDepartments() {
        if (!departments.value) {
            const value = await commonService.loadDepartments();
            if (value) {
                departments.value = value.payload;
            }
        }
    }

    async function loadProvinces() {
        if (!listProvinces.value) {
            const value = await commonService.loadProvinces();
            if (value) {
                listProvinces.value = value.payload;
            }
        }
    }

    async function loadCopyUsers() {
        if (!listCopyUsers.value) {
            const value = await commonService.loadCopyUsers();
            console.debug(value);
            if (value) {
                listCopyUsers.value = value.payload;
            }
        }
    }

    return {
        departments,
        listProvinces,
        listCopyUsers,
        loadDepartments,
        loadProvinces,
        loadCopyUsers
    };
});
