import BaseService from '@/service/BaseService';
import api from '@/constants/api';

export default class CommonService extends BaseService {
    static INSTANCE = new CommonService();

    async loadDepartments() {
        return await this.request(
            {
                path: api.COMMON.LOAD_DEPARTMENTS,
                method: 'GET'
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }

    async loadProvinces() {
        return await this.request(
            {
                path: api.COMMON.LOAD_PROVINCES,
                method: 'GET'
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }

    async loadDistrictsByProvince(idProvince) {
        return await this.request(
            {
                path: api.COMMON.LOAD_DISTRICTS_BY_PROVINCE + idProvince,
                method: 'GET'
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }

    async getDeliveryCost(idDistrict) {
        return await this.request(
            {
                path: api.COMMON.LOAD_DISTRICT_BY_DISTRICT + idDistrict,
                method: 'GET'
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }
}
