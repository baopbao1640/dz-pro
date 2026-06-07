<script setup lang="ts">
import { Card, Statistic, Row, Col, Typography } from 'ant-design-vue';

const { Title } = Typography;
</script>

<template>
  <div class="dashboard">
    <Title :level="4">工作台</Title>
    <Row :gutter="16">
      <Col :span="8">
        <Card>
          <Statistic title="项目数量" :value="0" />
        </Card>
      </Col>
      <Col :span="8">
        <Card>
          <Statistic title="用户数量" :value="0" />
        </Card>
      </Col>
      <Col :span="8">
        <Card>
          <Statistic title="待办事项" :value="0" />
        </Card>
      </Col>
    </Row>
    <Card :style="{ marginTop: 16 }">
      <p>Phase-1 骨架，业务功能开发中</p>
    </Card>
  </div>
</template>

<style scoped>
.dashboard {
}
</style>
