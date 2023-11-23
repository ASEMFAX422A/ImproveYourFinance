import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { RequestService } from '../../core/services/request.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-pdf-dialog',
  templateUrl: './pdf-dialog.component.html',
  styleUrls: ['./pdf-dialog.component.scss']
})
export class PdfDialogComponent {
  selectedFile: File | undefined;

  constructor(private requestService: RequestService, public dialog: MatDialog, public dialogRef: MatDialogRef<PdfDialogComponent>, private toastr: ToastrService) { }

  private checkFileType(fileToCheck: File): boolean {
    if (fileToCheck.type !== 'application/pdf') {
      this.toastr.error("Nur PDF Dateien erlaubt.", undefined, {positionClass: 'toast-center-center'});
      return false;
    }

    return true;
  }

  onFileChange(event: any): void {
    let currentFile = event.target.files[0];

    if (currentFile && this.checkFileType(currentFile)) {
      this.selectedFile = event.target.files[0];
    }
  }

  uploadFile(): void {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.requestService.post("bank-statement/upload", formData).subscribe(
        (response) => {
          console.log('File uploaded successfully', response);
          this.toastr.success("Erfolgreich hinzugefügt.");
        },
        (error) => {
          console.error('Error uploading file', error);
          this.toastr.error("Fehler beim hinzufügen!");
        }
      );
      this.dialogRef.close();
    }  else{
      this.toastr.warning("Keine Datei ausgewählt!");
    }
  }
  onClose() {
    
    this.dialogRef.close();
  }



  onDrop(event: any) {
    event.preventDefault();
    this.highlight(false);

    let currentFile = event.dataTransfer.files[0];
    if (currentFile && this.checkFileType(currentFile)) {
      this.selectedFile = event.target.files[0];
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

