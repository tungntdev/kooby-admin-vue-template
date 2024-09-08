import BaseService from '@/service/BaseService';
import API from '@/constants/api';

export default class DeathNumberService extends BaseService {
    static INSTANCE = new DeathNumberService();

    async getDeathNumbers(params) {
        return await this.request(
            {
                path: API.DEATH_RECORD.SEARCH,
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
                path: API.DEATH_RECORD.CREATE,
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
                path: API.DEATH_RECORD.UPDATE,
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

    async delete(id) {
        return await this.request(
            {
                path: API.DEATH_RECORD.DELETE + id,
                method: 'DELETE'
            },
            {
                secure: true,
                notifyOnSuccess: true,
                notifyOnError: true
            }
        );
    }

    async getNextDeathNumber() {
        const value = await this.request(
            {
                path: API.DEATH_RECORD.GET_NUMBER,
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
