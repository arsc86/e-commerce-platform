export class Constants {
    static readonly ERROR_MESSAGES = {
        INTERNAL_SERVER_ERROR: 'Internal server error. Please try again later.',
        UNEXPECTED_ERROR: 'Unexpected error occurred. Please try again later.',
        ERROR_CODE: (code: number) => `Unexpected error occurred. Please try again later. (Error code: ${code})`
    };

    static readonly MESSAGE_TYPES = {
        ERROR: 'error',
        SUCCESS: 'success',
        INFO: 'info',
        WARN: 'warn'
    };

    static readonly ROLES = {
        ADMIN          : 'ADMIN',
        PROVIDER       : 'PROVIDER',
        USER           : 'USER'
    };

    static readonly SESSION_VAL_TYPES = {
        ACCESSS_TOKEN: 'accessToken',
        REFRESH_TOKEN: 'refreshToken',
        USER_DATA    : '_dat',
        IS_VALID_ACCOUNT: '_val',
        IS_EXPIRED_ACCOUNT: '_exp'
    };

    static readonly ROUTING_PATHS = {
        HOME          : '/',
        LOGIN         : '/auth/login',
        VALIDATE_EMAIL: '/auth/validate/email',
        VERIFIED_EMAIL: '/auth/verified/email',
        CHANGE_PASS   : '/auth/change/password',
        SINGUP        : '/user/signup',
        USER_PROFILE  : '/user/profile'
    };
}