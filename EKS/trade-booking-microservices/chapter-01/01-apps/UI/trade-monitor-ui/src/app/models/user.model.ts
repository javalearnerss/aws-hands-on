export interface User {

  username: string
  email: string
  password: string
  tenantId: string
  enabled: boolean

  roles: number[]
  audiences: number[]
  scopes: number[]

}