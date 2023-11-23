import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import {CommonModule, NgIf} from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { ToastrModule } from 'ngx-toastr';
import { TransactionModule } from './extras/transaction/transaction.module';
import { FooterComponent } from './extras/footer/footer.component';
import { MaterialModule } from './material/material.module';
import { BankAccountService } from './core/services/bankAccountService';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MaterialModule,
    FooterComponent,
    TransactionModule,
    BrowserAnimationsModule,
    CommonModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      preventDuplicates: true,
      positionClass: 'toast-top-right',
      enableHtml: true
    })
    
  ],
  exports:[
    
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: MAT_DATE_LOCALE, useValue: 'de-DE' },
    BankAccountService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
