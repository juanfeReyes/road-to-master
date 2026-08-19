import { FormInputType } from "@/types/FormInputType"
import { Icon } from "@iconify/react"
import { DragEvent, ChangeEvent } from "react"
import { CustomDialog } from "../layout/CustomDialog"
import { FileViewer } from "../layout/FileViewer"
import { Label } from "./Label"
import { ErrorMessage } from "../layout/ErrorMessage"

type FileUploaderProps = {
    label?: string
    files: File[],
    error?: string,
    onChange: (files: File[]) => void
}


type FileOptionProps = {
    idx: number,
    file: File,
    handleFileRemoval: (idx: number) => void
}
const FileOption = ({ file, idx, handleFileRemoval }: FileOptionProps) => {

    return (<li
        key={`${file.name}`}
        className="flex gap-2 flex-col items-center bg-slate-50 max-w-28 rounded-2xl px-2">
        <CustomDialog
            button={(setIsOpen) => <div onClick={() => setIsOpen(true)}>
                <div className="flex w-full justify-center relative">
                    <Icon icon={"mdi:file"} className="text-2xl" />
                    <Icon icon={'typcn:delete'}
                        onClick={() => handleFileRemoval(idx)}
                        className="absolute top-0.5 right-0.5" />
                </div>
                {file.name}
            </div>}
            content={(setIsOpen) => <FileViewer file={file} />}
        />
    </li>)
}

export const FileUploader = ({ files, onChange, label, error }: FileUploaderProps) => {
    const acceptedFiles = ['.jpg', '.png', '.pdf', '.docx']

    const handleFileRemoval = (idx: number) => {
        const newFiles = files.toSpliced(idx, 1)
        onChange(newFiles)
    }

    const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault()
    }

    const handleDrops = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        const droppedFiles = Array.from(e.dataTransfer.files)
        onChange([...files, ...droppedFiles])
    }

    const handleOnFileSelectChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            onChange([...files, ...Array.from(e.target.files)])
        }
    }

    return (<>
        <Label
            label={label}
            content={
                <div
                    className="flex justify-evenly gap-2"
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
                    <ul className="flex flex-col gap-2">
                        {files.map((file, idx) => (<FileOption idx={idx} file={file} handleFileRemoval={handleFileRemoval} />))
                        }
                    </ul>
                </div>
            }
        />
        <ErrorMessage error={error} />
    </>
    )

}
