import { NgModule } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { PdfDialogComponent } from './pdf-dialog.component';



@NgModule({
  declarations: [PdfDialogComponent],
  imports: [
    CommonModule,
    NgIf
  ]
})
export class PdfDialogModule { }
