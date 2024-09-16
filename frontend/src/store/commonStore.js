import { defineStore } from 'pinia';
import { ref } from 'vue';
import CommonService from '@/service/CommonService';

const commonService = CommonService.INSTANCE;
export const useCommonStore = defineStore('commonStore', () => {
    const departments = ref();
    const listProvinces = ref();

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

    return {
        departments,
        listProvinces,
        loadDepartments,
        loadProvinces
    };
});
