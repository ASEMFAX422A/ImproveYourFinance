import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PdfDialogComponent } from './pdf-dialog.component';
import { ToastrModule } from 'ngx-toastr';



@NgModule({
  declarations: [PdfDialogComponent],
  imports: [
    CommonModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      preventDuplicates: true,
      positionClass: 'toast-top-right',
      enableHtml: true
    })
  ]
})
export class PdfDialogModule { }
