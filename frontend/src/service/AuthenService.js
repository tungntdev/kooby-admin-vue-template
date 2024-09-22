import BaseService from '@/service/BaseService';
import DEFAULTS from '@/constants/app';
import useAuthStore from '@/store/authStore';
import API from '@/constants/api';

const authStore = useAuthStore();

export default class AuthService extends BaseService {
    static INSTANCE = new AuthService();

    async login(params) {
        const res = await this.request(
            {
                path: API.AUTH.GENERATE_TOKEN,
                method: 'POST',
                data: params
            },
            {
                secure: false,
                notifyOnSuccess: true,
                notifyOnError: true
            }
        );
        if (res.state) {
            this.#updateAccessTokenInfo(res.payload);
        }
        return res.state;
    }

    #updateAccessTokenInfo(data) {
        localStorage.setItem(DEFAULTS.ACCESS.TOKEN, data);
        const token = JSON.parse(this.decodeUnicodeBase64(data.split('.')[1]));
        localStorage.setItem(DEFAULTS.ACCESS.EXPIRATION, token['exp']);
        localStorage.setItem(DEFAULTS.ACCESS.PERMISSIONS, JSON.stringify(token['$int_roles']));
        localStorage.setItem(DEFAULTS.PROFILE.FULL_NAME, token['display_name']);
        localStorage.setItem(DEFAULTS.PROFILE.FIRST_NAME, token['first_name']);
        localStorage.setItem(DEFAULTS.PROFILE.PROFILE_URL, token['picture_url']);
        authStore.updateExpiration();
        authStore.updatePermission();
    }

    decodeUnicodeBase64(base64) {
        const binaryString = atob(base64);
        const bytes = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        return new TextDecoder().decode(bytes);
    }
}
