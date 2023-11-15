import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {CommonModule, NgIf} from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { PdfDialogComponent } from './extras/pdf-dialog/pdf-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MatSlideToggleModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    NgIf,
    BrowserAnimationsModule,
    CommonModule,
    ReactiveFormsModule,
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
    { provide: MAT_DATE_LOCALE, useValue: 'de-DE' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
