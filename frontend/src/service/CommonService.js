import BaseService from '@/service/BaseService';

export default class CommonService extends BaseService {
    static INSTANCE = new CommonService();

    async loadDepartments() {
        return await this.request(
            {
                path: '/secure/departments',
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
