'use client'

import { NeutralButton, PrimaryButton } from "@/components/common/input/button/Button"
import { CustomInput } from "@/components/common/input/customInput/CustomInput"
import { PasswordInput } from "@/components/common/input/PasswordInput"
import { useNotification } from "@/components/common/interactivity/useNotification"
import { Header } from "@/components/common/layout/Header"
import { authClient } from "@/lib/auth-client"
import { useRouter } from "next/navigation"
import { useState } from "react"


export const SignUpForm = () => {
    const [email, setEmail] = useState('')
    const [pass, setPass] = useState('')
    const [confirmPass, setConfirmPass] = useState('')
    const [name, setName] = useState('')
    const [errors, setErrors] = useState({})
    const router = useRouter()
    const { notify } = useNotification()

    const handleSubmit = async () => {
        if (pass !== confirmPass) {
            const errorMsg = 'Password do not match'
            setErrors((errors) => ({...errors, pass: errorMsg, passConfirm: errorMsg}))
        }

        const { data, error } = await authClient.signUp.email({
            email: email,
            password: pass,
            name: name
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
        <Header icon="mdi:new-box" label="Join Us!" />
        <div className="flex flex-col gap-2">
            <CustomInput label="Name" value={name} onChange={(e) => setName(e)} error={errors['name']} />
            <CustomInput label="Email" value={email} onChange={(e) => setEmail(e)} error={errors['email']}/>
            <PasswordInput label="Password" value={pass} onChange={(e) => setPass(e)} error={errors['pass']}/>
            <PasswordInput label="Confirm password" value={confirmPass} onChange={(e) => setConfirmPass(e)} error={errors['passConfirm']} />
        </div>

        <div className="flex gap-4 justify-center">
            <NeutralButton label={'Cancel'} onClick={handleOnCancel} />
            <PrimaryButton label={'Submit'} onClick={handleSubmit} />
        </div>
    </div>)
}
