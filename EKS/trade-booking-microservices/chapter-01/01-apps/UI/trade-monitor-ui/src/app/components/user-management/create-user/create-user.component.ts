import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { UserForm } from '../../../types/user-form.type';


@Component({
  selector: 'app-create-user',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.css'
})
export class CreateUserComponent {

  userForm!: FormGroup<UserForm>;


  message = '';

  roles = [
    { id: 1, name: 'ADMIN' },
    { id: 2, name: 'USER' },
    { id: 3, name: 'VIEWER' }
  ];

  audiences = [
    { id: 1, name: 'trade-service' },
    { id: 2, name: 'payment-service' },
    { id: 3, name: 'analytics-service' }
  ];

  scopes = [
    { id: 1, name: 'trade.read' },
    { id: 2, name: 'trade.write' },
    { id: 3, name: 'payment.execute' }
  ];

  constructor(private fb: FormBuilder, private userService: UserService) {

    this.userForm = this.fb.group<UserForm>({
      username: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(4)]
      }),

      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email]
      }),

      password: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(8)]
      }),

      tenantId: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required]
      }),

      enabled: new FormControl(true, {
        nonNullable: true
      }),

      roles: new FormControl([], {
        nonNullable: true,
        validators: [Validators.required]
      }),

      audiences: new FormControl([], {
        nonNullable: true,
        validators: [Validators.required]
      }),

      scopes: new FormControl([], {
        nonNullable: true,
        validators: [Validators.required]
      })
    });

  }

  register() {

    const user = this.userForm.getRawValue();

    console.log(user);

    this.userService.registerUser(user)
      .subscribe({

        next: () => {
          this.message = "User registered successfully";
        },

        error: () => {
          this.message = "Registration failed";
        }

      });

  }

  get f() {
    return this.userForm.controls;
  }

  onRoleChange(event: any) {

    const roles = this.userForm.controls.roles.value;

    if (event.target.checked) {
      roles.push(+event.target.value);
    } else {
      const index = roles.indexOf(+event.target.value);
      roles.splice(index, 1);
    }

    this.userForm.controls.roles.setValue(roles);
  }


  onAudienceChange(event: any) {

    const audiences = this.userForm.controls.audiences.value;

    if (event.target.checked) {
      audiences.push(+event.target.value);
    } else {
      const index = audiences.indexOf(+event.target.value);
      audiences.splice(index, 1);
    }

    this.userForm.controls.audiences.setValue(audiences);
  }

  onScopeChange(event: any) {

    const scopes = this.userForm.controls.scopes.value;

    if (event.target.checked) {
      scopes.push(+event.target.value);
    } else {
      const index = scopes.indexOf(+event.target.value);
      scopes.splice(index, 1);
    }

    this.userForm.controls.scopes.setValue(scopes);
  }

}
