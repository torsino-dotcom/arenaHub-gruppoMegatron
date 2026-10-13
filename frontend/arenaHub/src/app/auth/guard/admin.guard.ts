import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../auth.service";

export const adminGuard: CanActivateFn = () => {
    const authService = inject(AuthService)
    const router = inject(Router)
    const user = authService.currentUser()

    if(!user) return router.createUrlTree(['/login'])

    if(user.ruolo !== 'admin') {
        return router.createUrlTree(['/'])
    }

    return true
}