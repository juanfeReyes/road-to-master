#!/bin/bash
sudo mkdir /etc/ecs
sudo bash -c "echo ECS_CLUSTER=r2m-containers-cluster >> /etc/ecs/ecs.config"