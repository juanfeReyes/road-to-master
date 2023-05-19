import { useState } from "react";

/**
 * Hook to create state of modal and state handlers
 * 
 * @returns open value, handleOpen to open modal and handleClose to close the modal
 */
export const useModalState = (): [boolean, () => void, () => void] => {
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return [open, handleOpen, handleClose]
}
