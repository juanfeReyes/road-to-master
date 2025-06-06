
#[derive(Debug)]
pub struct BoxTree {
  pub content: i32,
  pub children: Vec<Box<BoxTree>>
}
