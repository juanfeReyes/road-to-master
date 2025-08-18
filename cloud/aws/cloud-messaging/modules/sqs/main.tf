
resource "aws_iam_policy" "sqs_iam_policy" {
  
}

resource "aws_sqs_queue" "shipment_queue" {
  name = "r2m_shipment_queue"

}

resource "aws_sqs_queue_policy" "shipment_queue_policy" {
  queue_url = aws_sqs_queue.shipment_queue.id
  policy = jsondecode({
    Version: "2012-10-17",
    
  })
}

