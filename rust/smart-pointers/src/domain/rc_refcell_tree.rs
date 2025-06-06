use std::{cell::RefCell, rc::Rc};

#[derive(Debug)]
pub struct RcTree {
  pub content: i32,
  pub children: Vec<Rc<RefCell<RcTree>>>
}
