import { Component, OnInit, ViewChild } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { NgForm, FormControl } from '@angular/forms';
import { ProjectManagerService } from '../../shared/project-manager-service';
import { DatePipe } from '@angular/common';
declare var jQuery:any;

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit {
project: any = {};
    projectList: any = [];
    userList: any = [];
    searchText : string;
    inputParam: any;
    displayError: boolean = false;
    screenLoader: boolean;
    isAdd: boolean;
    selectedUserName: any;
    selectedUserId: number;
    isError: boolean;
    errorMessage: string;
    startDt: any;
    endDt: any;
    userDetailsMap: any = {};
    order: number = 1;  
    fieldName: string = '';
    minDate: Date = new Date();
    errorReason:string ="Please check back-end connectivity!!!";
    modalHeading: string;
    modalBody: string;
    @ViewChild('addProjectForm') addProjectForm: NgForm;

    constructor(public projectManagerService: ProjectManagerService, public router: Router, private datepipe: DatePipe) {
    }

    ngOnInit() {
      this.screenLoader = true;
      this.isAdd = true;
      this.isError = false;
      this.errorMessage = '';
      this.project = {};
      this.selectedUserName = '';
      this.project.priority = 0;
      this.getProjectDetails();
      this.getUserDetails();
    }
    getProjectDetails() {
        this.projectManagerService.getProject().subscribe(
          (data: any) => {
              if(null != data && undefined != data) {
                  this.projectList = data.projectVO;
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
    getUserDetails() {
        this.projectManagerService.getUser().subscribe(
          (data: any) => {
              if(null != data && undefined != data) {
                  this.userList = data.userVO;
                  if(null !== this.userList && undefined !== this.userList) {
                      for(var i = 0; i < this.userList.length; i++) {
                          this.userDetailsMap[this.userList[i].userId] = this.userList[i].fname + ', ' + this.userList[i].lname;
                      }
                  }
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
    addProject(proj: any) {
        this.screenLoader = true;
        if(!this.validateFields(proj)) {
            this.inputParam = {
                "action" : this.isAdd ? 'ADD' : 'EDIT',
                "projectVO" : {
                    "projectId" : this.isAdd ? 0 : proj.projectId,
                    "project" : proj.project,
                    "startDate" : proj.isSetDate ? this.datepipe.transform(proj.startDate, 'yyyy-MM-dd') : null,
                    "endDate" : proj.isSetDate ? this.datepipe.transform(proj.endDate, 'yyyy-MM-dd') : null,
                    "priority" : proj.priority,
                    "empId" : this.isAdd ? this.selectedUserId : proj.empId
                }
            };

            this.projectManagerService.updateProject(this.inputParam).subscribe(
                (data: any) => {
                    if(null != data && undefined != data && null !== data.status && undefined !== data.status && 'Success' === data.status) {
                        this.modalHeading="Success";
                        this.modalBody="Project Added/Updated SuccessFully";
                        this.submitModalPopup();
                        this.getProjectDetails();
                        this.getUserDetails();
                        this.screenLoader = false;
                    } else {
                        this.modalHeading="Failure";
                        this.modalBody="Sorry ! Unable to add/update the project";
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
            this.reset();
            this.router.navigate(['/addProject']);
        } else {
            this.screenLoader = false;
        }
    }

    updateProject(proj : any) {
        this.selectedUserName = this.userDetailsMap[proj.empId];
        this.project.startDate = this.datepipe.transform(proj.startDate, 'yyyy-MM-dd');
        this.project.endDate = this.datepipe.transform(proj.endDate, 'yyyy-MM-dd');

        this.project = proj;
        this.isAdd = false;
        this.isError = false;
    }

    suspendProject(proj : any) {
       // this.screenLoader = true;
        this.project = proj;
        
        this.inputParam = {
            "action" : "DELETE",
            "projectVO" : {
                "projectId" : proj.projectId
            }
        };

        this.projectManagerService.updateProject(this.inputParam).subscribe(
            (data: any) => {
                if(null != data && undefined != data && null !== data.status && undefined !== data.status && 'Success' === data.status) {
                    this.modalHeading="Success";
                    this.modalBody="Project deleted SuccessFully";
                    this.submitModalPopup();
                    this.getProjectDetails();
                    this.getUserDetails();
                    this.screenLoader = false;
                } else {
                    this.modalHeading="Failure";
                    this.modalBody="Sorry ! Unable to delete the project";
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
        this.reset();
        this.router.navigate(['/addProject']);
    }

    sortProject(prop: string) {
        this.order = this.order * (-1);
        let order_val = this.order == 1 ? 'asc' : 'desc';
        this.fieldName = prop + "-" + order_val;
        return false;
    }

    validateFields(proj: any) {
        this.isError = false;
        if(null !== proj && undefined !== proj) {
            if(null === proj.project || undefined === proj.project) {
                this.isError = true;
                this.errorMessage = 'Please enter Project Name';
                return this.isError;
            } else if((null === this.selectedUserId || undefined === this.selectedUserId) && (null === proj.empId || undefined === proj.empId)) {
                this.isError = true;
                this.errorMessage = 'Please select Manager';
                return this.isError;
            } else if(proj.isSetDate) {
                this.startDt = new Date(proj.startDate);
                this.endDt = new Date(proj.endDate);
                if(this.startDt > this.endDt) {
                    this.isError = true;
                    this.errorMessage = 'End date cannot be greater than Start date';
                    return this.isError;
                }
            } else {
                this.isError = false;
                return this.isError;
            }
        }
    }
    
    submitModalPopup(){
        jQuery("#submitModalWindowOpener").click();
    }

    getManager() {
        jQuery("#searchUserPopupOpener").click();
    }

     setStartEndDt() {
        this.minDate = new Date();
        this.project.startDate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
        this.project.endDate = this.datepipe.transform(new Date().getTime() + (60*60*24*1000), 'yyyy-MM-dd');
     }

    setUser(usr: any) {
        this.selectedUserName = usr.lname + ', ' + usr.fname;
        this.selectedUserId = usr.userId;
        this.isError = false;
        jQuery('#searchUserModalWindow').modal("hide");
    }

    reset() {
        this.project = {};
        this.project.priority = 0;
        this.isAdd = true;
        this.isError = false;
        this.selectedUserName = '';
        this.selectedUserId = 0;
        this.searchText = '';
        this.inputParam = {};
        this.displayError = false;
        this.errorMessage = '';
        this.addProjectForm.controls['projectName'].markAsPristine();
        this.addProjectForm.controls['projectName'].markAsUntouched();
    }


}
