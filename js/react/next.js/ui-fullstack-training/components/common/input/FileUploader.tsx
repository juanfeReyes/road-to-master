import { FormInputType } from "@/types/FormInputType"
import { Icon } from "@iconify/react"
import { useState, DragEvent, ChangeEvent } from "react"

type FileUploader = {

} & FormInputType

export const FileUploader = ({ form, setForm, inputKey, errors }: FileUploader) => {
    const acceptedFiles = ['.jpg', '.png', '.pdf', '.docx']

    const handleFileRemoval = (idx: number) => {
        const newFiles = form[inputKey].toSpliced(idx, 1)
        setForm({ ...form, [inputKey]: newFiles })
    }

    const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault()
    }

    const handleDrops = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        const droppedFiles = Array.from(e.dataTransfer.files)
        setForm({ ...form, [inputKey]: [...form[inputKey], ...droppedFiles] })
    }

    const handleOnFileSelectChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setForm({ ...form, [inputKey]: [...form[inputKey], ...Array.from(e.target.files)] })
        }
    }

    return (<div
        className="flex justify-evenly"
    >
        <div className="flex gap-3 w-2/3 border-2 rounded-2xl border-dashed p-1 justify-center items-center"
            onDrop={handleDrops}
            onDragOver={handleDragOver}>
            <label
                htmlFor="file-uploader"
                className="text-center"
            >Drop or select files</label>
            <input
                className="hidden"
                id="file-uploader"
                type="file"
                onChange={handleOnFileSelectChange}
                accept={acceptedFiles.join(',')}
                multiple
            />
        </div>
        <ul>
            {form[inputKey].map((file, idx) => (<li
                key={`${file.name}-${idx}`}
                onClick={() => handleFileRemoval(idx)}
                className="flex gap-2 flex-col  items-center">
                <Icon icon={"mdi:file"} className="text-2xl" />
                {file.name}
            </li>
            ))}
        </ul>
    </div>)

}
