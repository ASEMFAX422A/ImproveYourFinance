import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  templateUrl: './register.component.html',
  styleUrls: [ '../auth.style.scss' ]
})
export class RegisterComponent {
  protected registrationForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private toastr: ToastrService) {
    this.registrationForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(3)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*!,.])[A-Za-z\d@#$%^&*!,.]*$/)
      ]],
      confirmPassword: ['', [
        Validators.required
      ]]
    });
  }

  get username() {
    return this.registrationForm.get('username');
  }

  get password() {
    return this.registrationForm.get('password');
  }

  get confirmPassword() {
    return this.registrationForm.get('confirmPassword');
  }

  get isValid() {
    return this.registrationForm.valid && this.password?.value === this.confirmPassword?.value;
  }

  protected sendForm() {
    if (!this.isValid) {
      return;
    }

    const registerObject: any = {
      username: this.username?.value,
      password: this.password?.value
    };

    this.authService.register(registerObject).then(success => {
      if (success) {
        this.toastr.success('User erfolgreich erstellt!');
      } else {
        this.toastr.error('User existiert bereits!');
      }
    });
  }
}
