import AppConfig from '@/locales/vi/global/setting';
import Menu from '@/locales/vi/global/menu';
import Login from '@/locales/vi/views/login';
import Patient_request from '@/locales/vi/views/patient_request';
import Death_number_manager from '@/locales/vi/views/death_number_manager';
import Death_number_form_component from '@/locales/vi/views/death_number_form_component';
import Death_number_confirm_delete from '@/locales/vi/views/death_number_confirm_delete';
import Storage_manager from '@/locales/vi/views/storage_manager';
import Confirm_delete from '@/locales/vi/global/confirm_delete';
import Storage_component from '@/locales/vi/views/storage_component';
import Patient_reception_component from '@/locales/vi/views/patient_reception_component';
import Patient_reception_confirm from '@/locales/vi/views/patient_reception_confirm';
import Patient_reception_assignment from '@/locales/vi/views/patient_reception_assignment';
import Report_request from '@/locales/vi/views/report_request';
import Report_delivery from '@/locales/vi/views/report_delivery';
import Report_delivered from '@/locales/vi/views/report_delivered';
import Copy_list from '@/locales/vi/views/copy_list';
import Copy_record from '@/locales/vi/views/copy_record';
import Copy_confirm from '@/locales/vi/views/copy_confirm';
import Edit_copy_record from '@/locales/vi/views/edit_copy_record';

export default {
    'service.default-message.unknown-error': 'Đã có sự cố xảy ra. Vui lòng thử lại sau.',
    'service.default-message.response-status-400': 'Dữ hiệu không hợp lệ, không thể xử lý.',
    'service.default-message.response-status-401': 'Đăng nhập để truy cập tài nguyên này.',
    'service.default-message.response-status-403': 'Không đủ quyền để truy cập tài nguyên này.',
    'service.default-message.response-status-404': 'Tài nguyên này không tồn tại.',
    'service.default-message.api-error-network': 'Kết nối mạng có vấn đề.',
    'service.default-message.api-error-client': 'Trình duyệt không thể tạo kết nối.',
    'global.notification.default-title.error': 'Không thành công',
    'global.notification.default-title.warn': 'Cảnh báo',
    'global.notification.default-title.info': 'Thông tin',
    'global.notification.default-title.success': 'Thành công',
    ...AppConfig,
    ...Menu,
    ...Login,
    ...Patient_request,
    ...Death_number_manager,
    ...Death_number_form_component,
    ...Death_number_confirm_delete,
    ...Storage_manager,
    ...Confirm_delete,
    ...Storage_component,
    ...Patient_reception_component,
    ...Patient_reception_confirm,
    ...Patient_reception_assignment,
    ...Report_request,
    ...Report_delivery,
    ...Report_delivered,
    ...Copy_list,
    ...Copy_record,
    ...Copy_confirm,
    ...Edit_copy_record
};
