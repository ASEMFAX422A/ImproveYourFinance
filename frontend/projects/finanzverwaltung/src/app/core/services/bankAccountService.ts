import { Injectable } from '@angular/core';
import { RequestService } from './request.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BankAccountService {
  private bankAccounts = new BehaviorSubject<BankAccount[] | undefined>(undefined);
  public bankAccounts$ = this.bankAccounts.asObservable();
  
  private currentBankAccount = new BehaviorSubject<string>("all");
  public currentBankAccount$ = this.currentBankAccount.asObservable();

  constructor(private requestService: RequestService) {
    this.loadBankAccounts();
    console.log("bankAccService constructor");
  }


  loadBankAccounts(){
    this.requestService.post("bank-account/query-accounts",{}).subscribe(data=>{

      this.bankAccounts.next(data);
      console.log(this.bankAccounts);
    });
    
  }
  changeBankAccount(bankAccount:string){
    this.currentBankAccount.next(bankAccount);
  }
}


export interface BankAccount{
  iban:string;
}