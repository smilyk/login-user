# EMAIL-VERIFICATION (email-notification.jpg)
I. CHECK e-mail:

    -WebSecurity
    
II. EmailVertificationTokem:

    -UsersController -> verifyEmailToken

III. Создание и сохранение emailVerificationToken: 

    -Utils.generateEmailVertificationToken 

IV. Запрещаем вход тем пользователям, которые не подтвердили вход: 

    -UsersServiceImpl.loadUserByUsername

V. Отправка письма пользователю:

    -scared.AmazonSES
    -UsersServiceImpl.createUser   
    