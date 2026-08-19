'use client'

import { NeutralButton, PrimaryButton } from "@/components/common/input/button/Button"
import { CustomInput } from "@/components/common/input/customInput/CustomInput"
import { PasswordInput } from "@/components/common/input/PasswordInput"
import { useNotification } from "@/components/common/interactivity/useNotification"
import { Header } from "@/components/common/layout/Header"
import { authClient } from "@/lib/auth-client"
import { useRouter } from "next/navigation"
import { useState } from "react"
import * as z from 'zod'

const formSchema = z.object({
    email: z.email(),
    pass: z.string().min(9, 'Password is too short'),
    confirmPass: z.string().min(9, 'Confirm password is too short'),
    name: z.string().min(4, 'Name is too short')
})
    .refine((data) => data.pass === data.confirmPass,
        {
            message: 'Password does not match',
            path: ['pass', 'confirmPass']
        })

type Form = z.infer<typeof formSchema>;

export const SignUpForm = () => {
    const [form, setForm] = useState<Form>({
        name: '',
        pass: '',
        confirmPass: '',
        email: ''
    })
    const [errors, setErrors] = useState({})
    const router = useRouter()
    const { notify } = useNotification()

    const validate = () => {
        const result = formSchema.safeParse(form)
        if (result.error) {
            const errors = z.flattenError(result.error).fieldErrors
            setErrors(errors)
        }

        return result;
    }

    const handleSubmit = async () => {
        const result = validate()

        if (!result?.success) {
            return;
        }        

        const { data, error } = await authClient.signUp.email({
            email: result?.data.email,
            password: result?.data.pass,
            name: result?.data.name
        }, {
            onSuccess: () => {
                router.push('/tasks')
            },
            onError: (ctx) => {
                console.log(ctx)
                notify({ type: 'ERROR', value: ctx.error.message })
            }
        })
    }

    const handleOnCancel = () => {
        router.push('/')
    }

    return (<div className="flex flex-col gap-4 rounded-xl max-w-xl shadow-2xl px-2 py-4 bg-white">
        <Header icon="mdi:new-box" label="Join Us!" />
        <div className="flex flex-col gap-2">
            <CustomInput label="Name" value={form.name} onChange={(e) => setForm({...form, name: e})} error={errors['name']} />
            <CustomInput label="Email" value={form.email} onChange={(e) => setForm({...form, email: e})} error={errors['email']} />
            <PasswordInput label="Password" value={form.pass} onChange={(e) => setForm({...form, pass: e})} error={errors['pass']} />
            <PasswordInput label="Confirm password" value={form.confirmPass} onChange={(e) => setForm({...form, confirmPass: e})} error={errors['confirmPass']} />
        </div>

        <div className="flex gap-4 justify-center">
            <NeutralButton label={'Cancel'} onClick={handleOnCancel} />
            <PrimaryButton label={'Submit'} onClick={handleSubmit} />
        </div>
    </div>)
}
