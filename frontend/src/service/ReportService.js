import BaseService from '@/service/BaseService';
import api from '@/constants/api';

export default class ReportService extends BaseService {
    static INSTANCE = new ReportService();

    async patientRequestReport(params) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.REPORT_REQUEST,
                method: 'POST',
                data: params
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }

    async deliveryReport(params) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.REPORT_DELIVERY,
                method: 'POST',
                data: params
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }

    async deliveredReport(params) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.REPORT_DELIVERED,
                method: 'POST',
                data: params
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
    }
}
