import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
    constructor(private readonly auth: AuthService, private route: ActivatedRoute, private service: ProductServiceService ){}
    user: any;
    image:any;
    imageName:any;
    token: string = "";
    ngOnInit(): void {
      try {
        const token = localStorage.getItem('token');
        if (!token){
          throw new Error('No Token Found')
        }
  
        this.getUser(token);
        this.token = token
      } catch (error: any) {
        throw new Error(error)
      }
    }
    async getUser(token:string){
      this.user = (await this.auth.getUser(token))
      this.imageName = this.user.user.image
      
    }

    onFileSelected(event:Event){
      const userInput = event?.target as HTMLInputElement;
      if(userInput.files && userInput.files.length>0){
        this.image = userInput.files[0]
        console.log('Selected file:', this.image)
      }

    }
    async onUpload(){
      if (!this.image){
        console.error('No Image Selected')
        return;
      }
      const formData = new FormData()
      formData.append('image',this.image,this.image.name)
      console.log(formData.get("image"))
      try{
        await this.auth.uploadProfileImage(formData,this.token)
        console.log('File uploaded successfully');
      }catch(error){
        console.error('File upload failed', error);
      }
      

    }

}
