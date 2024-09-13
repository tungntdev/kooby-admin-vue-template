import BaseService from '@/service/BaseService';
import API from '@/constants/api';

export default class StorageService extends BaseService {
    static INSTANCE = new StorageService();

    async search(params) {
        return await this.request(
            {
                path: API.STORAGE.SEARCH,
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

    async create(params) {
        return await this.request(
            {
                path: API.STORAGE.CREATE,
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
                path: API.STORAGE.UPDATE + id,
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
                path: API.STORAGE.DELETE + id,
                method: 'DELETE'
            },
            {
                secure: true,
                notifyOnSuccess: true,
                notifyOnError: true
            }
        );
    }

    async getNumber() {
        const value = await this.request(
            {
                path: API.STORAGE.GET_NUMBER,
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
