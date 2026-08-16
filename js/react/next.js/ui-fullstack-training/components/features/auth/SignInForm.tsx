'use client'

import { Button } from "@/components/common/input/button/Button"
import { CustomInput } from "@/components/common/input/customInput/CustomInput"
import { PasswordInput } from "@/components/common/input/PasswordInput"
import { useNotification } from "@/components/common/interactivity/useNotification"
import { Header } from "@/components/common/layout/Header"
import { authClient } from "@/lib/auth-client"
import { useRouter } from "next/navigation"
import { useState } from "react"

export const SignInForm = () => {
const [email, setEmail] = useState('')
    const [pass, setPass] = useState('')
    const [errors, setErrors] = useState({})
    const router = useRouter()
    const { notify } = useNotification()

    const handleSubmit = async () => {

        const { data, error } = await authClient.signIn.email({
            email: email,
            password: pass,
        }, {
            onSuccess: () => {
                router.push('/tasks')
            },
            onError: (ctx) => {
                console.log(ctx)
                notify({type: 'ERROR', value: ctx.error.message})
            }
        })
    }

    const handleOnCancel = () => {
        router.push('/')
    }

    return (<div className="flex flex-col gap-4 rounded-xl max-w-xl shadow-2xl px-2 py-4 bg-white">
        <Header icon="ph:traffic-sign-thin" label="Sign In" />
        <div className="flex flex-col gap-2">
            <CustomInput label="Email" value={email} onChange={(e) => setEmail(e)} error={errors['email']}/>
            <PasswordInput label="Password" value={pass} onChange={(e) => setPass(e)} error={errors['pass']}/>
        </div>

        <div className="flex gap-4 justify-center">
            <Button type="Neutral" label={'Cancel'} onClick={handleOnCancel} />
            <Button type="Primary" label={'Sign In'} onClick={handleSubmit} />
        </div>
    </div>)
}
