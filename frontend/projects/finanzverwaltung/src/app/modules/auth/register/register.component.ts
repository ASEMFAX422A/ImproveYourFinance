import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { RequestService } from '../../../core/services/request.service';

@Component({
  templateUrl: './register.component.html',
  styleUrls: [ '../auth.style.scss' ]
})
export class RegisterComponent {
  protected registrationForm: FormGroup;

  constructor(private requestService: RequestService, private formBuilder: FormBuilder, private authService: AuthService, private toastr: ToastrService) {
    let usernameMinLength = 3;

    let passwordMinLength = 6;
    let passwordLowercase = 1;
    let passwordUppercase = 1;
    let passwordMinNumbers = 1;
    let passwordMinSpecialCharacters = 1;
    
    this.requestService.get("settings").subscribe((responseBody: any) => {
      usernameMinLength = responseBody?.usernameMinLength || usernameMinLength;
      
      passwordMinLength = responseBody?.passwordMinLength || passwordMinLength;
      passwordMinNumbers = responseBody?.passwordMinNumbers || passwordMinNumbers;
      passwordMinSpecialCharacters = responseBody?.passwordMinSpecialCharacters || passwordMinSpecialCharacters;
    });

    const regexString = `^(?=(?:[A-Z]*[a-z]*){${passwordLowercase},})(?=(?:[a-z]*[A-Z]*){${passwordUppercase},})(?=(?:\\D*\\d*){${passwordMinNumbers},})(?=(?:[!@#$%^&*,.]*[!@#$%^&*,.]){${passwordMinSpecialCharacters},})[A-Za-z\\d@#$%^&*!,.]*$`;

    this.registrationForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(usernameMinLength)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(passwordMinLength),
        Validators.pattern(regexString)
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
