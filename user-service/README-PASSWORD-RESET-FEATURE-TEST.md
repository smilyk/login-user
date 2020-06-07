#XX. PASSWORD-RESET-FEATURE (reset-password.jpg) + приложение verification-service
после отправчления запроса мы проверяем, есть ли пользователь в БД и если да, то генерируем токен на 1 день.

    - UsersController.requestReset
    - entity.PasswordResetRequestModel
    - UsersServiceImpl.requestPasswordReset
    - Utils.generatePasswordResetRequest
    
для проверки:

    - запрос на ..."/password-reset-request //на посту приходит письмо
    - нажимаем на ссылку в письме
    -.../verifiction-service/password-rest.html
    - ввод пароля
    -login -> 200OK