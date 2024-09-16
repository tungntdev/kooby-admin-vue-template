import BaseService from '@/service/BaseService';
import api from '@/constants/api';

export default class PatientRequestService extends BaseService {
    static INSTANCE = new PatientRequestService();

    async getPatientRequests(params) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.SEARCH,
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

    async create(params) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.CREATE,
                method: 'POST',
                data: params
            },
            {
                secure: true,
                notifyOnSuccess: true,
                notifyOnError: true
            }
        );
    }

    async update(params) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.UPDATE,
                method: 'PUT',
                data: params
            },
            {
                secure: true,
                notifyOnSuccess: true,
                notifyOnError: true
            }
        );
    }

    async delete(id) {
        return await this.request(
            {
                path: api.PATIENT_REQUEST.DELETE + id,
                method: 'DELETE'
            },
            {
                secure: true,
                notifyOnSuccess: true,
                notifyOnError: true
            }
        );
    }

    async getNextOrder() {
        let value = await this.request(
            {
                path: api.PATIENT_REQUEST.NEXT_PATIENT_ORDER,
                method: 'GET'
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
        if (value.state) {
            return value.payload;
        } else {
            return 1;
        }
    }

    async getDeliveryOrder() {
        let value = await this.request(
            {
                path: api.PATIENT_REQUEST.NEXT_DELIVERY_ORDER,
                method: 'GET'
            },
            {
                secure: true,
                notifyOnSuccess: false,
                notifyOnError: true
            }
        );
        if (value.state) {
            return value.payload;
        } else {
            return 1;
        }
    }
}
