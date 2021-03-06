import { Component, OnInit, ViewChild } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { NgForm, FormControl } from '@angular/forms';
import { ProjectManagerService } from '../../shared/project-manager-service';
declare var jQuery:any;
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html'
})
export class UserComponent implements OnInit {
    user: any = {};
    userList: any = [];
    searchText: string;
    isAdd: boolean;
    inputParam: any;
    displayError: boolean = false;
    screenLoader: boolean;
    order: number = 1;  
    fieldName: string = '';
    errorReason:string ="Please check back-end connectivity!!!";
    modalHeading: string;
    modalBody: string;
    @ViewChild('addUserForm') addUserForm: NgForm;
 
  constructor(public projectManagerService: ProjectManagerService, public router: Router) { }

  ngOnInit() {
        this.screenLoader = true;
        this.isAdd = true;
        this.getUserDetails();        
    }

    getUserDetails() {
        this.projectManagerService.getUser().subscribe(
          (data: any) => {
              if(null != data && undefined != data) {
                  this.userList = data.userVO;
                  this.screenLoader = false;
              } else {
                  this.screenLoader = false;
                  this.displayError = true;
              }
          },
          (err: any) => {
              this.screenLoader = false;
              this.displayError = true;
          }    
        );
    }

    addUser(usr: any) {
       // this.screenLoader = true;
        this.inputParam = {
            "action" : this.isAdd ? 'ADD' : 'EDIT',
            "userVO" : {
                "userId" : this.isAdd ? 0 : usr.userId,
                "fname" : usr.fname,
                "lname" : usr.lname,
                "empId" : usr.empId
            }
        };

        this.projectManagerService.updateUser(this.inputParam).subscribe(
            (data: any) => {
                if((null != data && undefined != data) && (null != data.status && undefined != data.status) && 'Success' === data.status) {
                     this.modalHeading="Success";
                     this.modalBody="User Added/Update Successfully";
                    this.submitModalPopup();
                    this.getUserDetails();
                    this.screenLoader = false;
                } else {
                    this.modalHeading="Failure";
                    this.modalBody="Sorry ! Unable to add/update the user";
                    this.submitModalPopup();
                    this.screenLoader = false;
                    this.displayError = true;
                }
            },
            (err: any) => {
                this.screenLoader = false;
                this.displayError = true;
            }    
        );
        this.user = {};
        this.reset();
        this.router.navigate(['/']);
    }

    editUser(usr : any) {
        this.user = usr;
        this.isAdd = false;
    }

    deleteUser(usr : any) {
        this.screenLoader = true;
        this.inputParam = {
            "action" : "DELETE",
            "userVO" : usr
        };

        this.projectManagerService.updateUser(this.inputParam).subscribe(
            (data: any) => {
                if(null != data && undefined != data && null !== data.status && undefined !== data.status && 'Success' === data.status) {
                    this.modalHeading="Success";
                    this.modalBody="User Deleted SuccessFully";
                    this.submitModalPopup();
                    this.getUserDetails();
                    this.screenLoader = false;
                } else {
                    this.modalHeading="Failure";
                     this.modalBody="Sorry ! Unable to delete the user";
                    this.submitModalPopup();
                    this.screenLoader = false;
                    this.displayError = true;
                }
            },
            (err: any) => {
                this.screenLoader = false;
                this.displayError = true;
            }    
        );
        this.user = {};
        this.reset();
        this.router.navigate(['/']);
    }
    
     submitModalPopup(){
        jQuery("#submitModalWindowOpener").click();
    }

    reset() {
        this.user = {};
        this.isAdd = true;
        this.searchText = '';
        this.inputParam = {};
        this.displayError = false;
        this.addUserForm.controls['fname'].markAsPristine();
        this.addUserForm.controls['fname'].markAsUntouched();
        this.addUserForm.controls['lname'].markAsPristine();
        this.addUserForm.controls['lname'].markAsUntouched();
        this.addUserForm.controls['empId'].markAsPristine();
        this.addUserForm.controls['empId'].markAsUntouched();
    }

    sortUser(prop: string) {
        this.order = this.order * (-1);
        let order_val = this.order == 1 ? 'asc' : 'desc';
        this.fieldName = prop + "-" + order_val;
        return false;
    }

}
