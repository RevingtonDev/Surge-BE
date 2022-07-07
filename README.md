# Surge - Back End

## `/` 

### `/credentials`

> **POST** - Authentication. Response header contains access token cookies.

> **DELETE** - Removes access token cookie and the token from database(if it contains).

### `/authorize?content=`

> **POST** - Checks if the client access token is valid.
> - content (boolean) - if the user details should return

### `/update `

> **PUT** - Updates temporary account details. 
>```
>{
>   firstName : string, 
>   lastName: string,
>   dateOfBirth: string(yyyy-mm-dd),
>   mobile: number,
>   password: string
>}
>```

---

## `/admin`

### `/create`

> **PUT** - Admin can create new accounts and on creation a notification email will be sent.
> ```
> {
>     email: string,
>     password: string,
>     accountType: string (admin | student)
> }
> ```


### `/users?page=&&type=&&limit=`

> **POST** - get users
> - page (number) - page number
> - limit (number) - users per page
> - type (string) - filter by account type ( . | admin | student )

### `/search?query=&&page=&&limit=`

> **POST** - search user by name, email or id
> - query (string) - search keyword
> - page (number) - page number
> - limit (number) - users per page

---

## `/student`

### `/note`, `/note?id=`

> **PUT** - create a new note
> ```
> {
>    title: string,
>    description: string
> }
> ```
 
> **DELETE** - delete a note
> - id (string) - note id

### `/note/upate`

> **PUT** - updates a note
>```
> {
>    id: string
>    title: string,
>    description: string
> }
> ```

### `/notes?page=&&limit=&&oldest=`

> **POST** - get all notes
> - page (number) - page number
> - limit (number) - notes per page
> -  oldest (boolean) - sort by oldest first