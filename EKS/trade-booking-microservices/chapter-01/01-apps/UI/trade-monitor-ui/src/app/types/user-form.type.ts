import { FormControl } from "@angular/forms";

export type UserForm = {
    username: FormControl<string>;
    email: FormControl<string>;
    password: FormControl<string>;
    tenantId: FormControl<string>;
    enabled: FormControl<boolean>;
    roles: FormControl<number[]>;
    audiences: FormControl<number[]>;
    scopes: FormControl<number[]>;
  };
