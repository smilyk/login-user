#I CREATE APP

I. создаем приложение

VI.Create UserRestControllerClass

VII. Adding MySql:

VII. USERS SIGN_UP:

    -UsersController -> createUser()
    -model.request.UserDetailsRequestModel // то что возвращаем
    -model.response.UserRest
    -entity.UserEntity
VIII. Spring Security For User-SIGN_UP

IX. USERS_SIGN_IN:

    -model.request.UserLoginRequestModel
    -UserServiceImpl реализовывает UserDetails
    -AuthenticationFilter
    -SpringApplicationContext
    
XII. Error - Handle Handle Exceptions:

    -AppExceptionsHandler
    -UserServiceException
XV. Разбиение на странички Paginations:

    -меняем getUsers()

XX. HATEOAS: можно добавлять в результаты ссылки на другие обьекты:

    - UserController -> getUserAddresses
    - UserController - >getUserAddress
    

# CORS
1. Над методом поставить @CorsOrigin(origins=**) - пройдут все запросы. Это как permitAll()
#
2. WebConfig
#
3.security.WebSecurity.CorsConfigurationSource
                                   

    
  

