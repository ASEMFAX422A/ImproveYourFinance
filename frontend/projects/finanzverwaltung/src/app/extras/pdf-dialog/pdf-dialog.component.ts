import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { RequestService } from '../../core/services/request.service';

@Component({
  selector: 'app-pdf-dialog',
  templateUrl: './pdf-dialog.component.html',
  styleUrls: ['./pdf-dialog.component.scss']
})
export class PdfDialogComponent {
  selectedFile: File | undefined;

  constructor(private requestService: RequestService, public dialog: MatDialog, public dialogRef: MatDialogRef<PdfDialogComponent>) { }

  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      
      if (this.selectedFile.type !== 'application/pdf') {
        alert("Nur PDF");
        this.selectedFile = undefined;
        return;
      }
    }
  }
  uploadFile(): void {
    if (this.selectedFile) {


      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.requestService.post("api/v1/upload/account-statement", formData).subscribe(
        (response) => {
          console.log('File uploaded successfully', response);
        },
        (error) => {
          console.error('Error uploading file', error);
        }
      );
    }
  }
  onClose() {
    this.dialogRef.close();
  }



  onDrop(event: any) {
    event.preventDefault();
    this.highlight(false);


    this.selectedFile = event.dataTransfer.files[0];
    if (this.selectedFile) {
      
      if (this.selectedFile.type !== 'application/pdf') {
        alert("Nur PDF");
        this.selectedFile = undefined;
        return;
      }
    }
  }

  onDragOver(event: any) {
    event.preventDefault();
    this.highlight(true);
  }

  onDragLeave(event: any) {
    event.preventDefault();
    this.highlight(false);
  }

  private highlight(isHighlighted: boolean) {
    // Add or remove a CSS class to highlight the drop area
    const dropArea = document.getElementById('drop-area');
    if (dropArea) {
      dropArea.classList.toggle('highlight', isHighlighted);
    }
  }
}

