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
    
    token: string = "";
    ngOnInit(): void {
      try {
        const token = localStorage.getItem('token');
        if (!token){
          throw new Error('No Token Found')
        }
  
        
        this.token = token
        this.getUser(this.token);
      } catch (error: any) {
        throw new Error(error)
      }
    }
    async getUser(token:string){
      this.user = (await this.auth.getUser(token))
      console.log(this.user.user.photo)
      const blob = (await this.auth.getphoto(this.user.user.image))
      this.image = URL.createObjectURL(blob)
      console.log("done")

      console.log(this.user)
      
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
        await this.auth.uploadProfileImage(this.token,formData)
        console.log('File uploaded successfully');
      }catch(error){
        console.error('File upload failed', error);
      }
      location.reload();

    }


}
