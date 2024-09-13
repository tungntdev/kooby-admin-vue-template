import AppConfig from '@/locales/en/global/setting';
import Menu from '@/locales/en/global/menu';
import Login from '@/locales/en/views/login';
import Patient_request from '@/locales/en/views/patient_request';
import Death_number_manager from '@/locales/en/views/death_number_manager';
import Death_number_form_component from '@/locales/en/views/death_number_form_component';
import Death_number_confirm_delete from '@/locales/en/views/death_number_confirm_delete';
import Storage_manager from '@/locales/en/views/storage_manager';
import Confirm_delete from '@/locales/en/global/confirm_delete';

export default {
    'service.default-message.unknown-error': 'There is something wrong, please comeback and try later.',
    'service.default-message.response-status-400': 'Submitted data could not be processed.',
    'service.default-message.response-status-401': 'Please sign in before access this resource.',
    'service.default-message.response-status-403': 'Permission denied for this resource.',
    'service.default-message.response-status-404': 'This resource does not exist.',
    'service.default-message.api-error-network': 'Internet connection is in trouble.',
    'service.default-message.api-error-client': 'Browser could not make request.',
    'global.notification.default-title.error': 'Failure',
    'global.notification.default-title.warn': 'Warning',
    'global.notification.default-title.info': 'Info',
    'global.notification.default-title.success': 'Success',
    ...AppConfig,
    ...Menu,
    ...Login,
    ...Patient_request,
    ...Death_number_manager,
    ...Death_number_form_component,
    ...Death_number_confirm_delete,
    ...Storage_manager,
    ...Confirm_delete
};
