import { defineStore } from 'pinia';
import { ref } from 'vue';
import CommonService from '@/service/CommonService';

const commonService = CommonService.INSTANCE;
export const useCommonStore = defineStore('commonStore', () => {
    const departments = ref();

    async function loadDepartments() {
        if (!departments.value) {
            const value = await commonService.loadDepartments();
            if (value) {
                departments.value = value.payload;
            }
        }
    }

    return {
        departments,
        loadDepartments
    };
});
